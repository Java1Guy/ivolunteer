/* Start ----------------------------------------------------- controllers/detail.js*/

// ==========================================================================
// App.DetailController
// ==========================================================================

require('core');

/** @class

  (Document Your View Here)

  @extends SC.ObjectController
  @author AuthorName
  @version 0.1
  @static
*/
App.detailController = SC.ObjectController.create(
/** @scope App.detailController */ {

  // TODO: Add your own code here.
  contentBinding: 'App.organizationListController.selection',
  
  commitChangesImmediately: false

}) ;


/* End ------------------------------------------------------- controllers/detail.js*/

