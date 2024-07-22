const routes = require('express').Router(),
    _ = require('lodash'),
    services = require('./services'),
    utils = require('./utils');

routes.get('/api/service/:serviceName', function(req, res) {
    if (_.has(services, req.params.serviceName) && typeof(_.get(services, req.params.serviceName)) == 'function') {
        var service = _.get(services, req.params.serviceName);
        try {
            var serviceResp = service();
            if (!_.isNil(serviceResp) && serviceResp instanceof Promise) {
                serviceResp.then((resp) => {
                    res.status(200).json(utils.success(resp));
                }).catch((err) => {
                    res.status(500).json(utils.error(err));
                })
            } else {
                res.status(200).json(utils.success(serviceResp));
            }
        } catch (e) {
            res.status(500).json(utils.error(e));
        }
    } else {
        res.status(404).json(utils.error("Service not found"));
    }

});

routes.all('*', function(req, res) {
    res.setHeader("Content-type", "text/html");
    res.status(404).send("<html><head><title>Page Not Found</title></head><body><h1>HTTP 404 : Page Not Found</h1><p>The requested URL was not found on the server</p><p>Request URL: " + req.params[0] + "</body></html>");
});

module.exports = routes;