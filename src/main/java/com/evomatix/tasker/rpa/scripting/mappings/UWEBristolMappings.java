package com.evomatix.tasker.rpa.scripting.mappings;

import java.util.Map;

public class UWEBristolMappings {

    private static final Map<String, String> decisionMapping = Map.of(
            "Conditional", AdventusDocumentTypes.Conditional_Offer,
            "Conditional Firm Accept", AdventusDocumentTypes.Conditional_Offer ,
            "Conditional Firm Accept Unconditional Firm Accept", AdventusDocumentTypes.Full_Offer ,
            "Unconditional Firm Accept", AdventusDocumentTypes.Full_Offer,
            "Further Info Request", AdventusDocumentTypes.More_Information_Requested,
            "Reject", AdventusDocumentTypes.Application_Outcome

    );


    private static final Map<String, String> orderTrackingMapping = Map.of(
            "Conditional", "Conditional",
            "Conditional Firm Accept", "Conditional",
            "Conditional Firm Accept Unconditional Firm Accept", "Full Offer",
            "Unconditional Firm Accept", "Full Offer"
    );

    public static String Conditional_Offer = "Dear Partner, Please be informed that we have received a conditional offer " +
            "letter from $University for $Course and the same has been attached under offer & " +
            "confirmation documents for your reference. Kindly share the same with the student and advise to read it " +
            "carefully and do the needful as soon as possible. Thank you";

    public static String Unconditional_Offer = "Dear Partner, Please be informed that we have received a Unconditional offer " +
            "letter from $University for $Course and the same has been attached under offer & " +
            "confirmation documents for your reference. Kindly share the same with the student and advise to read it " +
            "carefully and do the needful as soon as possible. Thank you";


    private static final Map<String, String> messageMapping = Map.of(
            "Conditional", UWEBristolMappings.Conditional_Offer,
            "Unconditional (Academically)", UWEBristolMappings.Unconditional_Offer
    );



    public static String getAdventusDocumentMapping(String decision){
        return UWEBristolMappings.getMapping(decisionMapping,decision);
    }

    public static String getAdventusOrderMapping(String decision){
        return UWEBristolMappings.getMapping(orderTrackingMapping,decision);
    }


    public static String getAdventusMessageMapping(String decision){
        return UWEBristolMappings.getMapping(messageMapping,decision);
    }


    private static String getMapping(Map<String,String> data, String key){

        if (data.containsKey(key)){
            return key;
        }else{
            return null;
        }

    }



}
