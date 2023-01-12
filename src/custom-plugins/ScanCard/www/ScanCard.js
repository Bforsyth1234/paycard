var exec = require('cordova/exec');

var PLUGIN_NAME = "ScanCard";

var ScanCard = function() {};

ScanCard.launchScanCard = function(options, onSuccess, onError) {
   exec(onSuccess, onError, PLUGIN_NAME, "launchScanCard", [options]);
};

module.exports = ScanCard;