/*******************************************************************************
 * Copyright (c) 2002, 2012 Innoopract Informationssysteme GmbH and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Innoopract Informationssysteme GmbH - initial API and implementation
 *    EclipseSource - ongoing development
 *    Frank Appel - replaced singletons and static fields (Bug 337787)
 ******************************************************************************/
package org.eclipse.rap.rwt.internal.lifecycle;

import org.eclipse.rap.rwt.internal.application.ApplicationContext;
import org.eclipse.rap.rwt.internal.util.ClassUtil;
import org.eclipse.rap.rwt.lifecycle.ILifeCycle;
import org.eclipse.rap.rwt.lifecycle.PhaseListener;


public class LifeCycleFactory {
  private static final Class<? extends LifeCycle> DEFAULT_LIFE_CYCLE_CLASS = SimpleLifeCycle.class;

  private final ApplicationContext applicationContext;
  private Class<? extends LifeCycle> lifeCycleClass;
  private LifeCycle lifeCycle;


  public LifeCycleFactory( ApplicationContext applicationContext ) {
    this.applicationContext = applicationContext;
    lifeCycleClass = DEFAULT_LIFE_CYCLE_CLASS;
  }

  public ILifeCycle getLifeCycle() {
    return lifeCycle;
  }

  public void configure( Class<? extends LifeCycle> lifeCycleClass ) {
    this.lifeCycleClass = lifeCycleClass;
  }

  public void activate() {
    lifeCycle = newLifeCycle();
    for( PhaseListener phaseListener : applicationContext.getPhaseListenerRegistry().getAll() ) {
      lifeCycle.addPhaseListener( phaseListener );
    }
  }

  public void deactivate() {
    lifeCycleClass = DEFAULT_LIFE_CYCLE_CLASS;
    lifeCycle = null;
  }

  private LifeCycle newLifeCycle() {
    Class< ? >[] argumentTypes = new Class<?>[] { ApplicationContext.class };
    Object[] arguments = new Object[] { applicationContext };
    return ClassUtil.newInstance( lifeCycleClass, argumentTypes, arguments );
  }
}