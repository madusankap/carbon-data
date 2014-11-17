package org.wso2.carbon.dssapi.core;

import org.wso2.carbon.dataservices.common.DBConstants;
import org.wso2.carbon.service.mgt.ServiceAdmin;
import org.wso2.carbon.service.mgt.ServiceMetaDataWrapper;

public class APIPublisher {
    /**
     * @param searchString
     * @param pageNumber
     * @return List of Data Services
     * @throws Exception
     */

    public ServiceMetaDataWrapper listDssServices(String searchString, int pageNumber) throws Exception {
        return new ServiceAdmin().listServices(DBConstants.DB_SERVICE_TYPE, searchString, pageNumber);
    }

/**        public void AddApi(APIModel apiModelal ){
 if(apiModelal!=null){
 ApiUtil apiUtil= new ApiUtil();
 apiUtil.writeApiTagInXML(apiModelal);
 }**/

}
