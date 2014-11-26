/*
 *  Copyright (c) 2005-2014, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */
package org.wso2.carbon.dssapi.ui;


import org.apache.axis2.AxisFault;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.dssapi.stub.APIPublisherException;
import org.wso2.carbon.dssapi.stub.APIPublisherStub;
import org.wso2.carbon.service.mgt.xsd.ServiceMetaData;
import org.wso2.carbon.service.mgt.xsd.ServiceMetaDataWrapper;

import java.rmi.RemoteException;

public class APIPublisherClient {

    private static Log log = LogFactory.getLog(APIPublisherClient.class);
    APIPublisherStub stub;
    ServiceMetaDataWrapper serviceMetaDataWrapper;

    public APIPublisherClient(String cookie, String url, ConfigurationContext configContext) throws AxisFault {
        String serviceEndpoint="";
        try {
            serviceEndpoint = url + "APIPublisher";
            stub = new APIPublisherStub(configContext,serviceEndpoint);
            ServiceClient client = stub._getServiceClient();
            Options option = client.getOptions();
            option.setManageSession(true);
            option.setProperty(org.apache.axis2.transport.http.HTTPConstants.COOKIE_STRING, cookie);

        } catch (AxisFault e ) {
            log.error("Error occurred while connecting via stub to : " + serviceEndpoint, e);
            throw e;

        }
    }

    /**
     *
     * @return number of pages
     * @throws RemoteException
     * @throws APIPublisherException
     */
    private int getNumberOfPages() throws RemoteException, APIPublisherException {
        serviceMetaDataWrapper = stub.listDssServices("", 0);
        return serviceMetaDataWrapper.getNumberOfPages();
    }

    /**
     *
     * @return number of active services
     * @throws RemoteException
     * @throws APIPublisherException
     */
    private int getNumberOfActiveServices() throws RemoteException, APIPublisherException {
        return stub.listDssServices("",0).getNumberOfActiveServices();
    }

    /**
     *
     * @return all the services
     * @throws RemoteException
     * @throws APIPublisherException
     */
    private ServiceMetaData[] getServices() throws RemoteException, APIPublisherException {
        return stub.listDssServices("",0).getServices();
    }

    /**
     *
     * @param serviceMetaData
     * @return api availability of the given service
     * @throws RemoteException
     */
    private boolean isAPIAvailable(ServiceMetaData serviceMetaData) throws RemoteException {
        return stub.apiAvailable(serviceMetaData.getName().toString());
    }


    /**
     *
     * @param serviceMetaData
     * @return status of the operation
     * @throws RemoteException
     * @throws APIPublisherException
     */
    private boolean publishAPI(ServiceMetaData serviceMetaData) throws RemoteException, APIPublisherException {
        String serviceId = serviceMetaData.getName().toString();
        return stub.addApi(serviceId);
    }

    /**
     *
     * @param serviceMetaData
     * @return status of the operation
     * @throws RemoteException
     */
    private boolean unpublishAPI(ServiceMetaData serviceMetaData) throws RemoteException {
        String serviceId = serviceMetaData.getName().toString();
        return stub.removeApi(serviceId);
    }






}
