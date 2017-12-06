/*
   Copyright 2017 MP Objects BV

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package com.mpobjects.labs.wicket.xrebelhub;

import org.apache.wicket.IRequestTarget;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.protocol.http.RequestLogger;
import org.apache.wicket.protocol.http.WebRequestCycle;
import org.apache.wicket.protocol.http.WebResponse;
import org.apache.wicket.request.target.component.IBookmarkablePageRequestTarget;
import org.apache.wicket.request.target.component.IPageRequestTarget;
import org.apache.wicket.request.target.component.listener.IListenerInterfaceRequestTarget;
import org.apache.wicket.request.target.resource.ISharedResourceRequestTarget;
import org.apache.wicket.util.lang.Classes;
import org.apache.wicket.util.string.AppendingStringBuffer;

/**
 *
 */
public class XrhRequestLogger extends RequestLogger {
	private static final String ENTRY_POINT_HEADER = "XRebelHub-EntryPoint";

	public XrhRequestLogger() {
		super();
	}

	@Override
	public void logEventTarget(IRequestTarget aTarget) {
		super.logEventTarget(aTarget);
		defineEntryPoint(aTarget);
	}

	private void defineEntryPoint(IRequestTarget aTarget) {
		RequestCycle requestCycle = RequestCycle.get();
		if (!(requestCycle instanceof WebRequestCycle)) {
			return;
		}
		String entryPoint = formatEntryPoint(aTarget);
		WebResponse response = ((WebRequestCycle) requestCycle).getWebResponse();
		response.setHeader(ENTRY_POINT_HEADER, entryPoint);
	}

	private String formatEntryPoint(IRequestTarget aTarget) {
		// Copy from org.apache.wicket.protocol.http.RequestLogger.getRequestTargetString(IRequestTarget)
		AppendingStringBuffer sb = new AppendingStringBuffer(128);
		if (aTarget instanceof IListenerInterfaceRequestTarget) {
			IListenerInterfaceRequestTarget listener = (IListenerInterfaceRequestTarget) aTarget;
			sb.append("Interface[target:");
			sb.append(Classes.simpleName(listener.getTarget().getClass()));
			sb.append("(");
			sb.append(listener.getTarget().getPageRelativePath());
			sb.append("), page: ");
			sb.append(listener.getPage().getClass().getName());
			sb.append("(");
			sb.append(listener.getPage().getId());
			sb.append("), interface: ");
			sb.append(listener.getRequestListenerInterface().getName());
			sb.append(".");
			sb.append(listener.getRequestListenerInterface().getMethod().getName());
			sb.append("]");
		} else if (aTarget instanceof IPageRequestTarget) {
			IPageRequestTarget pageRequestTarget = (IPageRequestTarget) aTarget;
			sb.append("PageRequest[");
			sb.append(pageRequestTarget.getPage().getClass().getName());
			sb.append("(");
			sb.append(pageRequestTarget.getPage().getId());
			sb.append(")]");
		} else if (aTarget instanceof IBookmarkablePageRequestTarget) {
			IBookmarkablePageRequestTarget pageRequestTarget = (IBookmarkablePageRequestTarget) aTarget;
			sb.append("BookmarkablePage[");
			sb.append(pageRequestTarget.getPageClass().getName());
			sb.append("(").append(pageRequestTarget.getPageParameters()).append(")");
			sb.append("]");
		} else if (aTarget instanceof ISharedResourceRequestTarget) {
			ISharedResourceRequestTarget sharedResourceTarget = (ISharedResourceRequestTarget) aTarget;
			sb.append("SharedResource[");
			sb.append(sharedResourceTarget.getResourceKey());
			sb.append("]");
		} else {
			sb.append(aTarget.toString());
		}
		return sb.toString();
	}
}
