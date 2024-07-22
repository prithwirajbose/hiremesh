module.exports.error = function(err) {
    return {
        success: false,
        result: err
    }
};

module.exports.success = function(result) {
    return {
        success: true,
        result: result
    }
};