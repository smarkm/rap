/*******************************************************************************
 * Copyright (c) 2012 EclipseSource and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    EclipseSource - initial API and implementation
 ******************************************************************************/

namespace( "org.eclipse.rwt.protocol" );

org.eclipse.rwt.protocol.Writer = function() {
  this._meta = {};
  this._operations = [];
  this._currentSetOperation = null;
  this._disposed = false;
};

org.eclipse.rwt.protocol.Writer.prototype = {

    dispose : function() {
      this._operations = null;
      this._meta = null;
      this._disposed = true;
    },

    hasOperations : function() {
      return this._operations.length > 0;
    },

    createMessage : function() {
      if( this._disposed ) {
        throw new Error( "Protocol message writer already disposed!" );
      }
      var message = {
        "meta" : this._meta,
        "operations" : this._operations
      };
      return JSON.stringify( message );
    },

    appendMeta : function( property, value ) {
      this._meta[ property ] = value;
    },

    appendSet : function( targetId, property, value ) {
      var properties = this._getPropertiesObjectFor( targetId );
      properties[ property ] = value;
    },

    appendCall : function( targetId, methodName, properties ) {
      this._currentSetOperation = null;
      this._operations.push( [ "call", targetId, methodName, properties ] );
    },

    _getPropertiesObjectFor : function( targetId ) {
      if( this._currentSetOperation === null || this._currentSetOperation[ 1 ] !== targetId ) {
        this._currentSetOperation = [ "set", targetId, {} ];
        this._operations.push( this._currentSetOperation );
      }
      return this._currentSetOperation[ 2 ];
    }

};