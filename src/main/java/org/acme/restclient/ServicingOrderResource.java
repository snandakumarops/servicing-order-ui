package org.acme.restclient;


import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.redhat.bian.servicedomain.models.CRCustomerEligibilityAssessmentEvaluateOutputModel;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;



@Path("/service")
public class ServicingOrderResource {

    @Inject
    @RestClient
    BianEligibilityService bianEligibilityService;

    @Inject
    @RestClient
    BianOfferProcedureInitiationService bianOfferProcedureInitiationService;


    @POST
    @Path("/eligibility-check")
    @Consumes(MediaType.APPLICATION_JSON)
    public String postCase(String json) {

        try {
            RequestObj mapVal = new Gson().fromJson(json,RequestObj.class);
            System.out.println(mapVal);
            System.out.println(mapVal.getCustomerReference());
            System.out.println(mapVal.getProductType());

            String body = " {\"data\": {\n" +
                    "        \"customerEligibilityAssessmentEvaluateActionRecord\": {},\n" +
                    "        \"customerEligibilityAssessmentInstanceRecord\": {\n" +
                    "         \"customerReference\":\""+mapVal.getCustomerReference()+"\",\n" +
                    "         \"productServiceType\":\""+mapVal.getProductType()+"\"\n" +
                    "        }\n" +
                    "      }\n" +
                    "    }";

            System.out.println(new Gson().fromJson(bianEligibilityService.checkEligibility(body),LinkedHashMap.class));

            LinkedHashMap map = new Gson().fromJson(bianEligibilityService.checkEligibility(body),LinkedHashMap.class);
            System.out.println(new Gson().toJson(map));
            Object data = map.get("data");
            System.out.println(new Gson().toJson(data));
            Map dataString = new Gson().fromJson(new Gson().toJson(data), Map.class);
            Object custEligibility = dataString.get("customerEligibilityAssessmentEvaluateActionRecord");
            Map custEligibilityString = new Gson().fromJson(new Gson().toJson(custEligibility), LinkedHashMap.class);
            String returnString = custEligibilityString.get("customerProductServiceTypeEligibility").toString();
            System.out.println("return"+returnString);
            return new Gson().toJson(returnString);

        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/initiateOffer")
    public String initiateOffer(String json) {
        try {
            RequestObj mapVal = new Gson().fromJson(json, RequestObj.class);
            System.out.println(mapVal);
            System.out.println(mapVal.getCustomerReference());
            System.out.println(mapVal.getProductType());

            String body = "{\"data\": {\n" +
                    "\t\t\"customerOfferProcedureInitiateActionRecord\": {},\n" +
                    "\t\t\"customerOfferProcedureInstanceRecord\": {\n" +
                    "\t\t\t\"customerReference\": \"" + mapVal.getCustomerReference() + "\",\n" +
                    "\t\t\t\"productServiceType\": \"" + mapVal.getProductType() + "\",\n" +
                    "\t\t\t\"customerOfferProcessingTask\": {\n" +
                    "\t\t\t\t\"customerOfferProcessingTaskType\": \"esignaturere\"\n" +
                    "\t\t\t}\n" +
                    "\t\t},\n" +
                    "\t\t\"disclosureInstanceRecord\": {\n" +
                    "\t\t\t\"disclosureType\": \"signaturerequired\",\n" +
                    "\t\t\t\"disclosureTextDescription\": \"Acceptance of e-wallet terms\",\n" +
                    "\t\t\t\"documentReference\": \"DISCLOSURE-76560\"\n" +
                    "\t\t},\n" +
                    "\t\t\"customerOfferProcedureInstanceStatus\": \"string\",\n" +
                    "\t\t\"customerOfferServicingSessionReference\": \"COSSR738028\",\n" +
                    "\t\t\"productInitializationInstanceRecord\": {\n" +
                    "\t\t\t\"productInstanceReference\": \"string\"\n" +
                    "\t\t}\n" +
                    "\t}\n" +
                    "}\n";
            LinkedHashMap map = new Gson().fromJson(bianOfferProcedureInitiationService.initiateOffer(body), LinkedHashMap.class);
            System.out.println(new Gson().toJson(map));
            Object data = map.get("data");
            System.out.println(new Gson().toJson(data));
            Map dataString = new Gson().fromJson(new Gson().toJson(data), Map.class);
            Object custEligibility = dataString.get("customerOfferProcedureInstanceRecord");
            Map custEligibilityString = new Gson().fromJson(new Gson().toJson(custEligibility), LinkedHashMap.class);
            Object returnObj = custEligibilityString.get("customerOfferProcessingTask");
            Map returnMap = new Gson().fromJson(new Gson().toJson(returnObj), LinkedHashMap.class);
            String returnString = returnMap.get("customerOfferProcessingTaskResult").toString();
            System.out.println(returnString);
            return new Gson().toJson(returnString);

        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



}