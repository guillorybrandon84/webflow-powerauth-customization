/*
 * Copyright 2017 Wultra s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.getlime.security.powerauth.app.dataadapter.controller;

import io.getlime.core.rest.model.base.request.ObjectRequest;
import io.getlime.core.rest.model.base.response.Response;
import io.getlime.security.powerauth.app.dataadapter.api.DataAdapter;
import io.getlime.security.powerauth.app.dataadapter.exception.DataAdapterRemoteException;
import io.getlime.security.powerauth.lib.dataadapter.model.entity.OperationChange;
import io.getlime.security.powerauth.lib.dataadapter.model.entity.OperationContext;
import io.getlime.security.powerauth.lib.dataadapter.model.request.OperationChangeNotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Controller class which handles notifications about changes of operation state.
 *
 * @author Roman Strobl, roman.strobl@wultra.com
 */
@RestController
@RequestMapping("/api/operation")
public class OperationChangeController {

    private static final Logger logger = LoggerFactory.getLogger(OperationChangeController.class);

    private DataAdapter dataAdapter;

    /**
     * Controller constructor.
     * @param dataAdapter Data adapter.
     */
    @Autowired
    public OperationChangeController(DataAdapter dataAdapter) {
        this.dataAdapter = dataAdapter;
    }

    /**
     * Receive a new operation change notification.
     *
     * @param request Request with change details.
     * @return Object response.
     * @throws DataAdapterRemoteException Thrown in case of remote communication errors.
     */
    @RequestMapping(value = "/change", method = RequestMethod.POST)
    public Response operationChangedNotification(@RequestBody ObjectRequest<OperationChangeNotificationRequest> request) throws DataAdapterRemoteException {
        logger.info("Received operationChangedNotification request for user: {}, operation ID: {}",
                request.getRequestObject().getUserId(), request.getRequestObject().getOperationContext().getId());
        OperationChangeNotificationRequest notification = request.getRequestObject();
        String userId = notification.getUserId();
        String organizationId = notification.getOrganizationId();
        OperationContext operationContext = notification.getOperationContext();
        OperationChange operationChange = notification.getOperationChange();
        dataAdapter.operationChangedNotification(userId, organizationId, operationChange, operationContext);
        logger.debug("The operationChangedNotification request succeeded");
        return new Response();
    }

}
