/* Start ----------------------------------------------------- core.js*/

// ==========================================================================
// App
// ==========================================================================

require('server/server') ;
IvServer = SC.Server.extend({

  _listSuccess: function(status, transport, cacheCode, context) {
    var json = eval('json='+transport.responseText) ;
    if (!json) { console.log('invalid json!'); return; }
    
    // first, build any records passed back
    if (json.organizations.records) {
      this.refreshRecordsWithData(json.records,context.recordType,cacheCode,false);
    }
    
    // next, convert the list of ids into records.
    var recs = (json.organizations.ids) ? json.ids.map(function(guid) {
      return SC.Store.getRecordFor(guid,context.recordType) ;
    }) : [] ;
    
    // now invoke callback
    if (context.callback) context.callback(recs,json.count,cacheCode) ;
  }
  
});

App = SC.Object.create({

  // This will create the server for your application.  Add any namespaces
  // your model objects are defined in to the prefix array.
  // server: SC.Server.create({ prefix: ['App'] }),
  server: IvServer.create({ prefix: ['App'], urlFormat: "server/%@/%@/" }),

  // When you are in development mode, this array will be populated with
  // any fixtures you create for testing and loaded automatically in your
  // main method.  When in production, this will be an empty array.
  FIXTURES: []

}) ;

/* End ------------------------------------------------------- core.js*/

