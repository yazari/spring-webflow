/*
 * Copyright 2004-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.faces.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
import javax.faces.render.Renderer;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * {@link Renderer} for the {@code <sf:commandButton>} tag.
 * 
 * @author Jeremy Grelle
 */
public class ProgressiveCommandButtonRenderer extends BaseDojoComponentRenderer {

	private static String[] ATTRIBUTES_TO_RENDER;

	private static String INPUT_TAG_NAME = "input";

	static {
		List<String> attributes = new ArrayList<String>();
		attributes.addAll(Arrays.asList(HTML.STANDARD_ATTRIBUTES));
		attributes.addAll(Arrays.asList(HTML.BUTTON_ATTRIBUTES));
		attributes.addAll(Arrays.asList(HTML.COMMON_ELEMENT_EVENTS));
		attributes.addAll(Arrays.asList(HTML.KEYBOARD_EVENTS));
		attributes.addAll(Arrays.asList(HTML.MOUSE_EVENTS));
		ATTRIBUTES_TO_RENDER = attributes.toArray(new String[attributes.size()]);
	}

	private Map<String, RenderAttributeCallback> attributeCallbacks;

	private final RenderAttributeCallback onclickCallback = new RenderAttributeCallback() {

		public void doRender(FacesContext context, ResponseWriter writer, UIComponent component, String attribute,
				Object attributeValue, String property) throws IOException {
			StringBuilder onclick = new StringBuilder();
			if (attributeValue != null) {
				String originalOnclick = attributeValue.toString().trim();
				if (!originalOnclick.endsWith(";")) {
					originalOnclick += ";";
				}
				onclick.append(originalOnclick);
			}

			Boolean ajaxEnabled = (Boolean) component.getAttributes().get("ajaxEnabled");
			Boolean disabled = (Boolean) component.getAttributes().get("disabled");
			String processIds = (String) component.getAttributes().get("processIds");
			if (Boolean.TRUE.equals(ajaxEnabled) && Boolean.FALSE.equals(disabled)) {
				if (StringUtils.hasText(processIds) && processIds.indexOf(component.getClientId(context)) == -1) {
					processIds = component.getClientId(context) + ", " + processIds;
				} else if (!StringUtils.hasText(processIds)) {
					processIds = component.getClientId(context);
				}
				onclick.append("Spring.remoting.submitForm('" + component.getClientId(context) + "', ");
				onclick.append("'" + RendererUtils.getFormId(context, component) + "', ");
				onclick.append("{processIds: '" + processIds + "'" + encodeParamsAsObject(context, component)
						+ "}); return false;");
			} else {
				onclick.append(getOnClickNoAjax(context, component));
			}

			if (onclick.length() > 0) {
				writer.writeAttribute(attribute, onclick.toString(), property);
			}
		}

	};

	protected Map<String, RenderAttributeCallback> getAttributeCallbacks(UIComponent component) {
		if (this.attributeCallbacks == null) {
			this.attributeCallbacks = new HashMap<String, RenderAttributeCallback>();
			this.attributeCallbacks.putAll(super.getAttributeCallbacks(component));
			this.attributeCallbacks.put("onclick", this.onclickCallback);
		}
		return this.attributeCallbacks;
	}

	/**
	 * This is a hook for subclasses to provide special onclick behavior in the non-ajax case
	 * @return the onclick value to use when Ajax is disabled.
	 */
	protected String getOnClickNoAjax(FacesContext context, UIComponent component) {
		// No special behavior necessary for CommandButton
		return "";
	}

	protected String[] getAttributesToRender(UIComponent component) {
		return ATTRIBUTES_TO_RENDER;
	}

	protected String getRenderedTagName(UIComponent component) {
		return INPUT_TAG_NAME;
	}

	public void decode(FacesContext context, UIComponent component) {
		if (context.getExternalContext().getRequestParameterMap().containsKey(component.getClientId(context))) {
			component.queueEvent(new ActionEvent(component));
		}
	}

	public boolean getRendersChildren() {
		return false;
	}

	protected String encodeParamsAsObject(FacesContext context, UIComponent component) {
		StringBuilder paramObj = new StringBuilder();
		for (int i = 0; i < component.getChildCount(); i++) {
			if (component.getChildren().get(i) instanceof UIParameter) {
				UIParameter param = (UIParameter) component.getChildren().get(i);
				Assert.hasText(param.getName(),
						"UIParameter requires a name when used as a child of a UICommand component");

				paramObj.append(", " + param.getName() + " : '" + param.getValue() + "'");
			}
		}
		return paramObj.toString();
	}

}
