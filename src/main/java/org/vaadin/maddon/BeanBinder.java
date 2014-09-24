/*
 * Copyright 2014 mattitahvonenitmill.
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
package org.vaadin.maddon;

/**
 * This class will hopefully be obsolete in V7.2 once my patch gets through our
 * insanely slow review process. https://dev.vaadin.com/review/#/c/2351/
 */
public class BeanBinder {

    public static <T> MBeanFieldGroup<T> bind(T bean, Object objectWithMemberFields) {
        MBeanFieldGroup<T> beanFieldGroup = new MBeanFieldGroup<T>((Class<T>) bean.getClass());
        beanFieldGroup.setItemDataSource(bean);
        beanFieldGroup.setBuffered(false);
        beanFieldGroup.bindMemberFields(objectWithMemberFields);
        beanFieldGroup.configureMaddonDefaults();
        return beanFieldGroup;
    }

}
