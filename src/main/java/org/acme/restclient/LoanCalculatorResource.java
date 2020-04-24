package org.acme.restclient;


import com.google.gson.Gson;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.util.LinkedHashMap;


@Path("/qualification-check")

public class LoanCalculatorResource {

    @Inject
    @RestClient
    LoanCalculatorService onboardingService;


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public ResultObj postCase(String json) {

        try {
            LinkedHashMap mapVal = new Gson().fromJson(json,LinkedHashMap.class);
            System.out.println(mapVal.keySet());
            String dmnReq = "{ \"model-namespace\" : \"https://kiegroup.org/dmn/_4D5ED131-974B-42A5-8997-9D416DD102E7\", " +
                    "\"model-name\" : \"Loan PreQualification\", \"dmn-context\" : {\"Credit Score\" : "+mapVal.get("creditScore")+", " +
                    "\"Down payment\": " +
                    mapVal.get("downPayment")+"," +
                    "\"Loan rate pct\":"+mapVal.get("loanRate")+"," +
                    "\"Monthly income\":"+mapVal.get("monthlyIncome")+",\"Purchase price\":"+mapVal.get("purchasePrice")+"}}";

             String response = onboardingService.checkQual(dmnReq);


            ResultObj resultObj = parseResults(response);


            return resultObj;

        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private ResultObj parseResults(String response) {
        LinkedHashMap resVal = new Gson().fromJson(response,LinkedHashMap.class);

        LinkedHashMap result = new Gson().fromJson(new Gson().toJson(resVal.get("result")),LinkedHashMap.class);

        LinkedHashMap dmnEvalResult = new Gson().fromJson(new Gson().toJson(result.get("dmn-evaluation-result")),LinkedHashMap.class);

        LinkedHashMap decisionresult = new Gson().fromJson(new Gson().toJson(dmnEvalResult.get("decision-results")),LinkedHashMap.class);

        LinkedHashMap finaldecision = new Gson().fromJson(new Gson().toJson(decisionresult.get("_2BBB81EB-DFA8-4E73-BC84-A25334E8FFED")),LinkedHashMap.class);


        ResultObj resultObj = new ResultObj();
        resultObj.setQualification(finaldecision.get("result").toString());
        LinkedHashMap dti = new Gson().fromJson(new Gson().toJson(decisionresult.get("_1431987D-1D84-494C-A918-D380D3F87AFE")),LinkedHashMap.class);
        resultObj.setDti(dti.get("result").toString());

        LinkedHashMap affordability = new Gson().fromJson(new Gson().toJson(decisionresult.get("_78978B61-5DC5-4363-8B96-264C4633B106")),LinkedHashMap.class);
        resultObj.setAffordability(affordability.get("result").toString());
        return resultObj;
    }

}