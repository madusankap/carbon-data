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

package org.wso2.carbon.dssapi.deployers;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.description.AxisModule;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.AxisServiceGroup;
import org.apache.axis2.description.Parameter;
import org.apache.axis2.engine.AxisConfiguration;
import org.apache.axis2.engine.AxisEvent;
import org.apache.axis2.engine.AxisObserver;
import org.wso2.carbon.dataservices.common.DBConstants;
import org.wso2.carbon.dataservices.core.engine.DataService;
import org.wso2.carbon.dssapi.util.APIUtil;

import java.util.ArrayList;
import java.util.logging.Logger;

public class ApiDeployer implements AxisObserver {
    APIUtil apiutil;
    private Logger logger = Logger.getLogger(ApiDeployer.class.getName());

    @Override
    public void init(AxisConfiguration axisConfiguration) {
        apiutil = new APIUtil();
    }

    @Override
    public void serviceUpdate(AxisEvent axisEvent, AxisService axisService) {
        switch (axisEvent.getEventType()) {
            case AxisEvent.SERVICE_DEPLOY: // handle new service deployment
                DataService dataService = getDataServiceObject(axisService);
                if (dataService != null) {
                    if (dataService.isManagedApi()) {
                        apiutil.addApi(dataService.getName());
                    } else {
                        apiutil.removeApi(dataService.getName());
                    }
                }
                break;
            case AxisEvent.SERVICE_REMOVE: // handle service removal
                break;
            case AxisEvent.SERVICE_START: // handle service start
                break;
            case AxisEvent.SERVICE_STOP: // handle service stop
                break;
        }
    }

    public DataService getDataServiceObject(AxisService axisService) {
        Parameter parameter = axisService.getParameter(DBConstants.DATA_SERVICE_OBJECT);
        return (DataService) parameter.getValue();
    }

    @Override
    public void serviceGroupUpdate(AxisEvent axisEvent, AxisServiceGroup axisServiceGroup) {
    }

    @Override
    public void moduleUpdate(AxisEvent axisEvent, AxisModule axisModule) {
    }

    @Override
    public void addParameter(Parameter parameter) throws AxisFault {
    }

    @Override
    public void removeParameter(Parameter parameter) throws AxisFault {
    }

    @Override
    public void deserializeParameters(OMElement omElement) throws AxisFault {

    }

    @Override
    public Parameter getParameter(String s) {
        return null;
    }

    @Override
    public ArrayList<Parameter> getParameters() {
        return null;
    }

    @Override
    public boolean isParameterLocked(String s) {
        return false;
    }
}
