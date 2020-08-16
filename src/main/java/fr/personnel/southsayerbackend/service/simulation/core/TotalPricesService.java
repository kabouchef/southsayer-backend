package fr.personnel.southsayerbackend.service.simulation.core;

import fr.personnel.southsayerbackend.model.simulation.PriceLine;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static fr.personnel.southsayerbackend.utils.global.MathUtils.arrondiMathematique;
import static fr.personnel.southsayerbackend.utils.global.MathUtils.multiplyDouble;

/**
 * @author Farouk KABOUCHE
 * Total Prices Service
 * @version 1.0
 */
@Slf4j
@Service
@Data
@With
@NoArgsConstructor
@AllArgsConstructor
public class TotalPricesService {
    double totalPriceHT = 0;
    double totalPriceTVAReduce = 0;
    double totalPriceTVAInter = 0;
    double totalPriceTVANormal = 0;
    double tvaReduceForbidden = 0;
    double tvaInterForbidden = 0;

    public void getTotalPrice(List<PriceLine> tarifPrestation) {
        totalPriceHT =
                tarifPrestation.stream()
                .map(x -> Double.parseDouble(x.getTarifPrestation()))
                .reduce(0.0d, Double::sum);

        tvaReduceForbidden =
                tarifPrestation.stream()
                        .map(x -> Double.parseDouble(x.getTvaReduite()))
                        .filter(x -> x < 1).count();

        tvaInterForbidden =
                tarifPrestation.stream()
                        .map(x -> Double.parseDouble(x.getTvaInter()))
                        .filter(x -> x < 1).count();

        if (tvaReduceForbidden == 0)
            totalPriceTVAReduce = arrondiMathematique(multiplyDouble(totalPriceHT, 1.055));

        if (tvaInterForbidden == 0)
            totalPriceTVAInter = arrondiMathematique(multiplyDouble(totalPriceHT, 1.1));

        totalPriceTVANormal = arrondiMathematique(multiplyDouble(totalPriceHT, 1.2));
    }
}
