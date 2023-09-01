package gz.helpp.Model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModelSession {
    private static final Logger logger = LoggerFactory.getLogger(ModelSession.class);

    private static ModelSession instance;
    private ModelUser modelUser;

    private ModelSession() {}

    public static ModelSession getInstance() {
        if (instance == null) {
            instance = new ModelSession();
        }
        return instance;
    }

    public static Logger getLogger(){
        return logger;
    }

    public void setModelUser(ModelUser modelUser){
        this.modelUser = modelUser;
    }

    public ModelUser getModelUser(){
        return this.modelUser;
    }

}