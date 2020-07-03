package fr.personnel.southsayerbackend.service;

import fr.personnel.southsayerbackend.model.PriceLine;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static fr.personnel.southsayerbackend.configuration.utils.MathUtils.arrondiMathematique;
import static fr.personnel.southsayerbackend.configuration.utils.MathUtils.multiplyDouble;

/**
 * @author Farouk KABOUCHE
 * <p>
 * Total Prices Service
 */
@Slf4j
@Service
@Data
@NoArgsConstructor
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
                .map(x -> Double.parseDouble(x.getTarif_prestation()))
                .reduce(0.0d, (x, y) -> x + y);

        tvaReduceForbidden =
                tarifPrestation.stream()
                        .map(x -> Double.parseDouble(x.getTva_reduite()))
                        .filter(x -> x < 1).count();

        tvaInterForbidden =
                tarifPrestation.stream()
                        .map(x -> Double.parseDouble(x.getTva_inter()))
                        .filter(x -> x < 1).count();

        if (tvaReduceForbidden == 0)
            totalPriceTVAReduce = arrondiMathematique(multiplyDouble(totalPriceHT, 1.055));

        if (tvaInterForbidden == 0)
            totalPriceTVAInter = arrondiMathematique(multiplyDouble(totalPriceHT, 1.1));

        totalPriceTVANormal = arrondiMathematique(multiplyDouble(totalPriceHT, 1.2));
    }
}
