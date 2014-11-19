/*
 *  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
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
package org.wso2.carbon.dssapi.common;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.wso2.carbon.dataservices.core.admin.DataServiceAdmin;
import org.wso2.carbon.dataservices.ui.beans.Data;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ApiUtil  {
    public void ManagedApi(boolean managedApi,String serviceId) throws Exception {
        String serviceContents = "";
        serviceContents = new DataServiceAdmin().getDataServiceContentAsString(serviceId);
        InputStream ins = new ByteArrayInputStream( serviceContents.getBytes());
        OMElement configElement = (new StAXOMBuilder(ins)).getDocumentElement();
        configElement.build();
        Data data = new Data();
        data.populate(configElement);
        data.setManagedApi(managedApi);
        new DataServiceAdmin().saveDataService(data.getName(),data.getServiceHierarchy(),data.buildXML().toString());
    }

}
