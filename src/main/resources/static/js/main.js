'use strict';
var simulationCode = document.getElementById("simulationCodeInput").value;

window.addEventListener("load", function () {

    showLoader();

    function download() {
        var filename = "PRICE_FROM_" + simulationCode + ".xls";
        /*        var element = document.createElement('a');
                /!*element.setAttribute('href', 'data:text/plain;charset=utf-8,' /!*+ encodeURIComponent(text)*!/);*!/
                console.log(filename);
                element.setAttribute('href', '../xls/'+ filename);

                element.setAttribute('download', filename);

                element.style.display = 'none';
                document.body.appendChild(element);

                element.click();

                document.body.removeChild(element);

                document.getElementById("export").innerHTML = "";*/

        var exportFile = document.createElement("a");
        exportFile.href = '../xls/' + filename;
        exportFile.textContent = "Export - " + filename;
        var exportUrl = document.querySelector('#export a');
        if (exportUrl) {
            document.getElementById("export").replaceChild(exportFile, exportUrl);
        } else {
            document.getElementById("export").appendChild(exportFile);
        }

        removeIframe();

        addIframeSource("PRICE_FROM_" + simulationCode + ".html");
        showLoader();
    }

    function addIframeSource(nameSource) {
        /*document.getElementById('preview-result').setAttribute("src", nameSource);*/
        document.querySelector('[class="preview hidden"]').classList.remove('hidden');


        /*const page = frames['iframe'].document;
        const linkSheetStyle = document.createElement('link');
        linkSheetStyle.setAttribute("rel", "stylesheet");
        linkSheetStyle.setAttribute("href", "css/iframe.css");
        page.head.appendChild(linkSheetStyle);*/

        /*var cssLink = document.createElement("link");
        cssLink.href = "../css/iframe.css";
        cssLink.rel = "stylesheet";
        cssLink.type = "text/css";
        frames[0].document.head.appendChild(cssLink);*/
    }

    function removeIframe(nameSource) {
        var divClass = document.querySelector('.preview');
        if (divClass.getAttribute("class") === "preview") {
            divClass.classList.add("hidden");
        }
    }

    function sendData() {
        var XHR = new XMLHttpRequest();

        // Liez l'objet FormData et l'élément form
        var FD = new FormData(form);

        // Définissez ce qui se passe si la soumission s'est opérée avec succès
        XHR.addEventListener("load", function (event) {

        });

        XHR.onreadystatechange = function () {
            if (XHR.readyState === 4) {
                var response =  JSON.parse(XHR.response);
                var index = 0,
                    totalPriceHT = 0,
                    tvaAllowedReduite = 0,
                    tvaAllowedInter = 0,
                    tvaAllowedNormale = 0;

                /*var createTrElement = document.createElement("tr");
                var createTdElement = document.createElement("td");*/
                var elementTr = document.querySelector(".preview-export tbody");
                var elementTd = document.querySelectorAll(".preview-export tbody tr")[index];

                response.forEach(element => {
                    var priceTableTr = document.createElement("tr");
                    var priceTableIdentifiant = document.createElement("td");
                    var priceTableDetail_prestation = document.createElement("td");
                    var priceTableQuantite = document.createElement("td");
                    var priceTableTarif_unitaire = document.createElement("td");
                    var priceTableTarif_prestation = document.createElement("td");
                    var priceTableType_prestation = document.createElement("td");
                    var priceTablePrestation_de = document.createElement("td");
                    var priceTableTva_reduite = document.createElement("td");
                    var priceTableTva_inter = document.createElement("td");
                    var priceTableTva_normale = document.createElement("td");
                    var priceTableCode_49 = document.createElement("td");
                    var priceTableCod_type_prestation = document.createElement("td");
                    var priceTableTemp_pose = document.createElement("td");
                    var priceTableOrdre = document.createElement("td");

                    totalPriceHT += parseInt(element.tarif_prestation);
                    tvaAllowedReduite += parseInt(element.priceTableTva_reduite);
                    tvaAllowedInter += parseInt(element.priceTableTva_inter);
                    tvaAllowedNormale += parseInt(element.priceTableTva_normale);

                    priceTableIdentifiant.textContent = element.identifiant;
                    priceTableDetail_prestation.textContent = element.detail_prestation;
                    priceTableQuantite.textContent = element.quantite;
                    priceTableTarif_unitaire.textContent = element.tarif_unitaire;
                    priceTableTarif_prestation.textContent = element.tarif_prestation;
                    priceTableType_prestation.textContent = element.type_prestation;
                    priceTablePrestation_de.textContent = element.prestation_de;
                    priceTableTva_reduite.textContent = element.tva_reduite;
                    priceTableTva_inter.textContent = element.tva_inter;
                    priceTableTva_normale.textContent = element.tva_normale;
                    priceTableCode_49.textContent = element.code_49;
                    priceTableCod_type_prestation.textContent = element.cod_type_prestation;
                    priceTableTemp_pose.textContent = element.temp_pose;
                    priceTableOrdre.textContent = element.ordre;

                    document.querySelector(".preview-export tbody").appendChild(priceTableTr);
                    document.querySelectorAll(".preview-export tbody tr")[index].appendChild(priceTableIdentifiant);
                    document.querySelectorAll(".preview-export tbody tr")[index].appendChild(priceTableDetail_prestation);
                    document.querySelectorAll(".preview-export tbody tr")[index].appendChild(priceTableQuantite);
                    document.querySelectorAll(".preview-export tbody tr")[index].appendChild(priceTableTarif_unitaire);
                    document.querySelectorAll(".preview-export tbody tr")[index].appendChild(priceTableTarif_prestation);
                    document.querySelectorAll(".preview-export tbody tr")[index].appendChild(priceTableType_prestation);
                    document.querySelectorAll(".preview-export tbody tr")[index].appendChild(priceTablePrestation_de);
                    document.querySelectorAll(".preview-export tbody tr")[index].appendChild(priceTableTva_reduite);
                    document.querySelectorAll(".preview-export tbody tr")[index].appendChild(priceTableTva_inter);
                    document.querySelectorAll(".preview-export tbody tr")[index].appendChild(priceTableTva_normale);
                    document.querySelectorAll(".preview-export tbody tr")[index].appendChild(priceTableCode_49);
                    document.querySelectorAll(".preview-export tbody tr")[index].appendChild(priceTableCod_type_prestation);
                    document.querySelectorAll(".preview-export tbody tr")[index].appendChild(priceTableTemp_pose);
                    document.querySelectorAll(".preview-export tbody tr")[index].appendChild(priceTableOrdre);

                    index++;

                });

                var createTrElement = document.createElement("tr");
                var createTdElement = document.createElement("td");
                createTdElement.textContent = " ";
                document.querySelector(".preview-export tbody").appendChild(createTrElement);
                document.querySelectorAll(".preview-export tbody tr")[index].appendChild(createTdElement);
                document.querySelectorAll(".preview-export tbody tr")[index].appendChild(createTdElement);
                document.querySelectorAll(".preview-export tbody tr")[index].appendChild(createTdElement);
                document.querySelectorAll(".preview-export tbody tr")[index].appendChild(createTdElement);


                var totalPriceEntete = document.createElement("td");
                totalPriceEntete.textContent = "Total HT";
                document.querySelectorAll(".preview-export tbody tr")[index]
                    .appendChild(totalPriceEntete);
                var totalPriceTotal = document.createElement("td");
                totalPriceTotal.textContent = totalPriceHT.toString() + " €";
                document.querySelectorAll(".preview-export tbody tr")[index]
                    .appendChild(totalPriceTotal);

                if(tvaAllowedReduite == tvaAllowedNormale){
                    var totalPriceReduite = 0;
                    totalPriceReduite = totalPriceHT * 1.055;
                    var createTrElement = document.createElement("tr");
                    var elementTr = document.querySelector(".preview-export tbody");
                    elementTr.appendChild(createTrElement);
                    var totalPriceEntete = document.createElement("td");
                    totalPriceEntete.textContent = "Total 5.5%";
                    document.querySelectorAll(".preview-export tbody tr")[index]
                        .appendChild(totalPriceEntete);
                    var totalPriceTotal = document.createElement("td");
                    totalPriceTotal.textContent = totalPriceReduite.toString() + " €";
                    document.querySelectorAll(".preview-export tbody tr")[index]
                        .appendChild(totalPriceTotal);
                }

                if(tvaAllowedInter == tvaAllowedNormale) {
                    var totalPriceInter = 0;
                    var totalPriceInter = totalPriceHT * 1.1;
                    var createTrElement = document.createElement("tr");
                    var elementTr = document.querySelector(".preview-export tbody");
                    elementTr.appendChild(createTrElement);
                    var totalPriceEntete = document.createElement("td");
                    totalPriceEntete.textContent = "Total 10%";
                    document.querySelectorAll(".preview-export tbody tr")[index]
                        .appendChild(totalPriceEntete);
                    var totalPriceTotal = document.createElement("td");
                    totalPriceTotal.textContent = totalPriceInter.toString() + " €";
                    document.querySelectorAll(".preview-export tbody tr")[index]
                        .appendChild(totalPriceTotal);
                }

                var totalPriceNormale = 0;
                totalPriceNormale = totalPriceHT * 1.2;
                var createTrElement = document.createElement("tr");
                var elementTr = document.querySelector(".preview-export tbody");
                elementTr.appendChild(createTrElement);
                var totalPriceEntete = document.createElement("td");
                totalPriceEntete.textContent = "Total 20%";
                document.querySelectorAll(".preview-export tbody tr")[index]
                    .appendChild(totalPriceEntete);
                var totalPriceTotal = document.createElement("td");
                totalPriceTotal.textContent = totalPriceNormale.toString() + " €";
                document.querySelectorAll(".preview-export tbody tr")[index]
                    .appendChild(totalPriceTotal);

                download();
            }
        }

        // Definissez ce qui se passe en cas d'erreur
        XHR.addEventListener("error", function (event) {
            alert('Oups! Quelque chose s\'est mal passé.');
        });

        // Configurez la requête
        XHR.open("POST", "/request");

        // Les données envoyées sont ce que l'utilisateur a mis dans le formulaire
        XHR.send(FD);
    }

    // Accédez à l'élément form …
    var form = document.getElementById("simulationForm");

    function showLoader() {
        document.querySelector('.loader-container').classList.add('hidden');
    }

    // … et prenez en charge l'événement submit.
    form.addEventListener("submit", function (event) {
        event.preventDefault();
        sendData();
    });

});

function loadingLoader() {
    document.querySelector('.loader-container').classList.remove('hidden');
}

document.getElementById("submitSimulation").addEventListener("click", loadingLoader);
