/*
 * Copyright 2009-2014 PrimeTek.
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
package net.ruesse.idc.control;

import java.io.Serializable;
import javax.faces.FacesException;
import javax.faces.application.ViewExpiredException;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;

/**
 * 
 * @author Ulrich Rüße <ulrich@ruesse.net>
 */
@RequestScoped
@Named
public class ExceptionHandlerView implements Serializable{
    
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public void throwNullPointerException() {
        throw new NullPointerException("A NullPointerException!");
    }

    /**
     * 
     */
    public void throwWrappedIllegalStateException() {
        Throwable t = new IllegalStateException("A wrapped IllegalStateException!");
        throw new FacesException(t);
    }

    /**
     * 
     */
    public void throwViewExpiredException() {
        throw new ViewExpiredException("A ViewExpiredException!",
                FacesContext.getCurrentInstance().getViewRoot().getViewId());
    }
}
