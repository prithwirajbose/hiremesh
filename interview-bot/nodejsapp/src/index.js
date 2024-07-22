var express = require('express'),
    bodyParser = require('body-parser'),
    cors = require('cors'),
    favicon = require('serve-favicon'),
    path = require('path'),
    fs = require('fs-extra'),
    rewrite = require('express-urlrewrite'),
    _ = require('lodash');
require('dotenv').config();
//Include routes and controllers
routes = require('./routes');

var PORT = process.env.PORT || 8080;


//Create Express App
var app = express();

//Parse Input as JSON
app.use(bodyParser.json({ extended: true }));
//catch express errors
app.use(cors());

app.use((err, req, res, next) => {
    console.error(err);
    res.status(500).send("An error has occured! Please try again.");
});

/*app.use(rewrite('/app/css/*', '/css/$1'));
app.use(rewrite('/app/js/*', '/js/$1'));
app.use(rewrite('/app/images/*', '/images/$1'));
app.use(rewrite('/app/resources/*', '/resources/$1'));
app.use(rewrite('/app/*', '/$1.html'));
app.use(rewrite('/app/', '/index.html'));*/
app.use(favicon(path.join(__dirname, 'pages', 'favicon.ico')));
app.use('/', express.static(path.join(__dirname, 'pages'), { extensions: ['html'] }));

//Setup Routes
app.use('/', routes);


var server = app.listen(PORT, function() {
    console.log("Q&A Bot running at http://localhost:" + PORT);
});

module.exports = server;