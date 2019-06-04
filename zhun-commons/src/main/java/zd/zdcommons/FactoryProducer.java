package zd.zdcommons;

import zd.zdcommons.abstractFactory.AnalysisAbstractFactory;
import zd.zdcommons.facotry.AnalysisFactory;
import zd.zdcommons.facotry.ReadFacotory;

public class FactoryProducer {
    public static AnalysisAbstractFactory getFactory(String choice){
        if(choice.equalsIgnoreCase("Analysisze")){
            return new AnalysisFactory();
        } else if(choice.equalsIgnoreCase("Read")){
            return new ReadFacotory();
        }
        return null;
    }
}
