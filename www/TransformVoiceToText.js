var exec = require('cordova/exec');

exports.transform = function(arg0, success, error) {
    exec(success, error, "TransformVoiceToText", "transform", [arg0]);
};
