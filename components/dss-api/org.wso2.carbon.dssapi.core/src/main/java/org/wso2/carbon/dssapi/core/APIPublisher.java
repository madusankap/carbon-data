package org.wso2.carbon.dssapi.core;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.wso2.carbon.dataservices.common.DBConstants;
import org.wso2.carbon.dataservices.core.admin.DataServiceAdmin;
import org.wso2.carbon.dataservices.ui.beans.Data;
import org.wso2.carbon.dssapi.util.APIUtil;
import org.wso2.carbon.service.mgt.ServiceAdmin;
import org.wso2.carbon.service.mgt.ServiceMetaDataWrapper;

import java.io.ByteArrayInputStream;
import java.io.InputStream;


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

    /**
     *
     * @param ServiceName
     * @return availability of api to DataServices
     */
    public boolean apiAvailable(String ServiceName){
        return new APIUtil().apiAvailable(ServiceName);
    }

    /**
     *
     * @param serviceId
     * @throws Exception
     */
       public void AddApi(String serviceId) throws Exception {
           String serviceContents = "";
           serviceContents = new DataServiceAdmin().getDataServiceContentAsString(serviceId);
           InputStream ins = new ByteArrayInputStream(serviceContents.getBytes());
           OMElement configElement = (new StAXOMBuilder(ins)).getDocumentElement();
           configElement.build();
          Data data = new Data();
           data.populate(configElement);
           data.setManagedApi(true);
           new DataServiceAdmin().saveDataService(serviceId, "", data.buildXML().toString());
         //  new APIUtil().addApi(data.getName());
 }

    /**
     *
     * @param serviceId
     * @throws Exception
     * this will
     */
    public void removeApi(String serviceId)throws Exception{
        String serviceContents = "";
        serviceContents = new DataServiceAdmin().getDataServiceContentAsString(serviceId);
        InputStream ins = new ByteArrayInputStream(serviceContents.getBytes());
        OMElement configElement = (new StAXOMBuilder(ins)).getDocumentElement();
        configElement.build();
        Data data = new Data();
        data.populate(configElement);
        data.setManagedApi(false);
        new DataServiceAdmin().saveDataService(serviceId,"",data.buildXML().toString());
    }
}
