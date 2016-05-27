package com.cotech.taxislibres.payu;

import com.google.gson.Gson;

import co.com.taxislibres.pojo.PayUCommon;
import co.com.taxislibres.pojo.token.BasicResponse;
import co.com.taxislibres.pojo.token.CreditCardTokenRequest;
import co.com.taxislibres.pojo.token.CreditCardTokenResponse;
import co.com.taxislibres.pojo.token.PaymentTokenRequest;
import co.com.taxislibres.pojo.token.PaymentTokenResponse;
import co.com.taxislibres.pojo.token.RemoveCreditCardToken;
import co.com.taxislibres.rest.TaxisLibresPayUTokens;
import co.com.taxislibres.util.Utilidades;

public class PagoPayu {
	
	private static String apiLogin = "11959c415b33d0c";			//Para Pruebas
    private static String apiKey = "6u39nqhq8ftd0hlvnjfs66eh8c";
    private static String payerId = "APPROVED";
	
    
    public void payUCommonMethod(PayUCommon pPayUCommon) {
        pPayUCommon.getMerchant().setApiLogin(apiLogin);
        pPayUCommon.getMerchant().setApiKey(apiKey);
        pPayUCommon.setLanguage("es");
        pPayUCommon.setTest(true);
    }
    
    public void ping(PayUCommon pPayUCommon) {
        pPayUCommon.setCommand("PING");
        String jsonPing = TaxisLibresPayUTokens.ejecutarComandoPayURest(pPayUCommon);
        System.out.println("[PruebaRest.ping] jsonPing = " + jsonPing);
    }

    public static BasicResponse createToken(PayUCommon pPayUCommon) {
        Gson gson = new Gson();
        pPayUCommon.setCommand("CREATE_TOKEN");
        pPayUCommon.setCreditCardToken(new CreditCardTokenRequest());
        pPayUCommon.setCreditCardTokenInformation(null);
        pPayUCommon.setRemoveCreditCardToken(null);
        pPayUCommon.getCreditCardToken().setPayerId(payerId);

//        // Tarjeta 1   f9993dbe-a34b-4edb-bb55-2568a764db6a    // Pruebas 10fd3839-d26b-48e6-af58-2e04b5154be4
        pPayUCommon.getCreditCardToken().setName("full name");
        pPayUCommon.getCreditCardToken().setIdentificationNumber("32144457");
        pPayUCommon.getCreditCardToken().setPaymentMethod("VISA");
        pPayUCommon.getCreditCardToken().setNumber("4111111111111111");
        pPayUCommon.getCreditCardToken().setExpirationDate("2015/02");
        // Tarjeta 2   
//        pPayUCommon.getCreditCardToken().setName("full name");
//        pPayUCommon.getCreditCardToken().setIdentificationNumber("32144457");
//        pPayUCommon.getCreditCardToken().setPaymentMethod("MASTERCARD");
//        pPayUCommon.getCreditCardToken().setNumber("5127360584181767");
//        pPayUCommon.getCreditCardToken().setExpirationDate("2015/01");
        //Tar 3   "688"
        //        pPayUCommon.getCreditCardToken().setName("full name");
//        pPayUCommon.getCreditCardToken().setIdentificationNumber("32144457");
//        pPayUCommon.getCreditCardToken().setPaymentMethod("VISA");
//        pPayUCommon.getCreditCardToken().setNumber("4506680005011713");
//        pPayUCommon.getCreditCardToken().setExpirationDate("2016/05");
        //    Tar 4 BBVA Bancomer  funciono   567    bf542211-a2cc-447e-842d-87e824be6295
//        pPayUCommon.getCreditCardToken().setName("MARITZA M. HERNANDEZ");
//        pPayUCommon.getCreditCardToken().setIdentificationNumber("HEHM860228");
//        pPayUCommon.getCreditCardToken().setPaymentMethod("VISA");
//        pPayUCommon.getCreditCardToken().setNumber("4772913003225051");
//        pPayUCommon.getCreditCardToken().setExpirationDate("2019/01");
        
        //    Tar 6 b bOGOTOTA  990
//    pPayUCommon.getCreditCardToken().setName("MARITZA M HERNANDEZ H");
//    pPayUCommon.getCreditCardToken().setNumber("5120690004527414");
//    pPayUCommon.getCreditCardToken().setExpirationDate("2019/12");
//    pPayUCommon.getCreditCardToken().setIdentificationNumber("HEHM860228");
//    pPayUCommon.getCreditCardToken().setPaymentMethod("MASTERCARD");
        
                //    Tar 7 b bOGOTOTA  990
//    pPayUCommon.getCreditCardToken().setName("MARITZA M HERNANDEZ H");
//    pPayUCommon.getCreditCardToken().setNumber("5120690004527414");
//    pPayUCommon.getCreditCardToken().setExpirationDate("2019/12");
//    pPayUCommon.getCreditCardToken().setIdentificationNumber("HEHM860228");
//    pPayUCommon.getCreditCardToken().setPaymentMethod("MASTERCARD");
        
        
        //    Tar 8 Banco Azteca   "160";   ed60a008-ee95-46ec-ab67-b10c33241876
//        pPayUCommon.getCreditCardToken().setName("MARITZA MILENA HERNANDEZ HERNANDEZ");
//        pPayUCommon.getCreditCardToken().setIdentificationNumber("HEHM860228");
//        pPayUCommon.getCreditCardToken().setNumber("4027662683794927");
//        pPayUCommon.getCreditCardToken().setExpirationDate("2016/08");
//        pPayUCommon.getCreditCardToken().setPaymentMethod("VISA");
    
        //Tar 9 611    f4ca09a3-336f-4749-9da8-4f0c5561fb1c
//        pPayUCommon.getCreditCardToken().setName("MARITZA HERNANDEZ H");
//        pPayUCommon.getCreditCardToken().setNumber("370782519851005");
//        pPayUCommon.getCreditCardToken().setExpirationDate("2018/05");
//        pPayUCommon.getCreditCardToken().setIdentificationNumber("HEHM860228");
//        pPayUCommon.getCreditCardToken().setPaymentMethod("AMEX");

        String jsonCreateToken = TaxisLibresPayUTokens.ejecutarComandoPayURest(pPayUCommon);
        System.out.println("[PruebaRest.createToken] jsonCreateToken = " + jsonCreateToken);

        return gson.fromJson(jsonCreateToken, BasicResponse.class);

    }

    public static BasicResponse consultarToken(PayUCommon pPayUCommon) {
        Gson gson = new Gson();
        pPayUCommon.setCommand("GET_TOKENS");
        pPayUCommon.setCreditCardToken(null);
        pPayUCommon.setRemoveCreditCardToken(null);
        pPayUCommon.setCreditCardTokenInformation(new CreditCardTokenResponse());
        pPayUCommon.getCreditCardTokenInformation().setPayerId(payerId);

        // Igualar a null si se quiere traer todos los tokens de este PayerId
        pPayUCommon.getCreditCardTokenInformation().setCreditCardTokenId(null);

        // Se debe igualar a Null los siguientes campos
        pPayUCommon.getCreditCardTokenInformation().setCreationDate(null);
        pPayUCommon.getCreditCardTokenInformation().setErrorDescription(null);
        pPayUCommon.getCreditCardTokenInformation().setMaskedNumber(null);
        pPayUCommon.getCreditCardTokenInformation().setName(null);
        pPayUCommon.getCreditCardTokenInformation().setIdentificationNumber(null);
        pPayUCommon.getCreditCardTokenInformation().setPaymentMethod(null);
        pPayUCommon.getCreditCardTokenInformation().setNumber(null);
        pPayUCommon.getCreditCardTokenInformation().setExpirationDate(null);

        String jsonConsultarToken = TaxisLibresPayUTokens.ejecutarComandoPayURest(pPayUCommon);
        System.out.println("[PruebaRest.consultarToken] jsonConsultarToken = " + jsonConsultarToken);

        return gson.fromJson(jsonConsultarToken, BasicResponse.class);

    }

    public static BasicResponse eliminarToken(PayUCommon pPayUCommon) {
        Gson gson = new Gson();
        pPayUCommon.setCommand("REMOVE_TOKEN");
        pPayUCommon.setCreditCardToken(null);
        pPayUCommon.setCreditCardTokenInformation(null);
        pPayUCommon.setRemoveCreditCardToken(new RemoveCreditCardToken());

        pPayUCommon.getRemoveCreditCardToken().setPayerId(payerId);
        pPayUCommon.getRemoveCreditCardToken().setCreditCardTokenId("f9993dbe-a34b-4edb-bb55-2568a764db6a");

        String jsonEliminarToken = TaxisLibresPayUTokens.ejecutarComandoPayURest(pPayUCommon);
        System.out.println("[PruebaRest.eliminarToken] jsonEliminarToken = " + jsonEliminarToken);

        return gson.fromJson(jsonEliminarToken, BasicResponse.class);
    }

    public static PaymentTokenResponse submitTransaction() {
        Gson gson = new Gson();
        PaymentTokenRequest paymentTokenRequest = new PaymentTokenRequest();
        paymentTokenRequest.setCommand("SUBMIT_TRANSACTION");
        paymentTokenRequest.setLanguage("es");
        paymentTokenRequest.setTest(true);
        paymentTokenRequest.getMerchant().setApiLogin(apiLogin);
        paymentTokenRequest.getMerchant().setApiKey(apiKey);
        //paymentTokenRequest.getTransaction().getOrder().setAccountId("509276"); //Mexico
        paymentTokenRequest.getTransaction().getOrder().setAccountId("500537");  // Panama
        paymentTokenRequest.getTransaction().getOrder().setReferenceCode("factura0001");
        paymentTokenRequest.getTransaction().getOrder().setDescription("Test order Colombia");
        paymentTokenRequest.getTransaction().getOrder().setLanguage("es");
        paymentTokenRequest.getTransaction().getOrder().getShippingAddress().setCountry("CO");
        paymentTokenRequest.getTransaction().getOrder().getShippingAddress().setPhone("5517517105");
        paymentTokenRequest.getTransaction().getOrder().getBuyer().setFullName("full name");
        paymentTokenRequest.getTransaction().getOrder().getBuyer().setEmailAddress("marith20@gmail.com");
        paymentTokenRequest.getTransaction().getOrder().getBuyer().setDniNumber("32144457");
        paymentTokenRequest.getTransaction().getOrder().getBuyer().getShippingAddress().setCountry("MX");
        paymentTokenRequest.getTransaction().getOrder().getBuyer().getShippingAddress().setPhone("5517517105");
        paymentTokenRequest.getTransaction().getPayer().setFullName("full name");
        paymentTokenRequest.getTransaction().getPayer().setEmailAddress("marith20@gmail.com");
        paymentTokenRequest.getTransaction().getOrder().getAdditionalValues().getTx_value().setValue("39");
        //paymentTokenRequest.getTransaction().getOrder().getAdditionalValues().getTx_value().setCurrency("MXN");
        paymentTokenRequest.getTransaction().getOrder().getAdditionalValues().getTx_value().setCurrency("USD");

        paymentTokenRequest.getTransaction().getOrder().setSignature(Utilidades.encryptarMd5(apiKey, "500238",
                paymentTokenRequest.getTransaction().getOrder().getReferenceCode(), paymentTokenRequest.getTransaction().getOrder().getAdditionalValues().getTx_value().getValue(), paymentTokenRequest.getTransaction().getOrder().getAdditionalValues().getTx_value().getCurrency()));

        paymentTokenRequest.getTransaction().setCreditCardTokenId("aca09507-7485-45e0-8fb4-f7d3d7a905c2");
        paymentTokenRequest.getTransaction().getCreditCard().setSecurityCode("123");
        paymentTokenRequest.getTransaction().setType("AUTHORIZATION_AND_CAPTURE");
        paymentTokenRequest.getTransaction().setIpAddress("201.217.202.179");
        //paymentTokenRequest.getTransaction().setIpAddress("181.135.6.29");

        paymentTokenRequest.getTransaction().setPaymentMethod("VISA");
        paymentTokenRequest.getTransaction().setPaymentCountry("CO");
        
        
        

        String jsonSubmitTransaction = TaxisLibresPayUTokens.ejecutarPagoToken(paymentTokenRequest);
        
        System.out.println("[PruebaRest.submitTransaction] jsonSubmitTransaction = " + jsonSubmitTransaction);

        return gson.fromJson(jsonSubmitTransaction, PaymentTokenResponse.class);

    }


}
