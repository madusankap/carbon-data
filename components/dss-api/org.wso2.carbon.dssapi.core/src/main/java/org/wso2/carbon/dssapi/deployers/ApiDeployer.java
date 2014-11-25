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

/**
 * Created by tharindud on 11/20/14.
 */
public class ApiDeployer implements AxisObserver {

    @Override
    public void init(AxisConfiguration axisConfiguration) {
    }

    @Override
    public void serviceUpdate(AxisEvent axisEvent, AxisService axisService) {
        switch (axisEvent.getEventType())
        {
            case AxisEvent.SERVICE_DEPLOY : // handle new service deployment

                break;
            case AxisEvent.SERVICE_REMOVE : // handle service removal

                break;
            case AxisEvent.SERVICE_START : // handle service start
                DataService dataService =getDataServiceObject(axisService);
                if(dataService!=null){
                    if(dataService.isManagedApi()){
                        new APIUtil().addApi(dataService.getName());
                    }
                }
                break;
            case AxisEvent.SERVICE_STOP : // handle service stop
                break;
        }

    }

    private DataService getDataServiceObject(AxisService axisService) {
        Parameter parameter=axisService.getParameter(DBConstants.DATA_SERVICE_OBJECT);
        return (DataService)parameter.getValue();
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