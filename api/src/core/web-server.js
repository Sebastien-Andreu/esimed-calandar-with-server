const express = require('express');
const { initializeConfigMiddlewares, initializeErrorMiddlwares } = require('./middlewares');
const authRoutes = require('../controllers/auth-routes');
//const meetRoutes = require('../controllers/meet-routes');
const path = require("path");

class WebServer {
  app = undefined;
  port = 1234;
  server = undefined;

  constructor() {
    this.app = express();

    initializeConfigMiddlewares(this.app);
    this._initializeRoutes();
    initializeErrorMiddlwares(this.app);
  }

  start() {
    this.server = this.app.listen(this.port, () => {
      console.log(`Example app listening on port ${this.port}`);
    });
  }

  _initializeRoutes() {
    this.app.use(authRoutes.initializeRoutes())
//    this.app.use('/meet', meetRoutes.initializeRoutes())
  }
}

module.exports = WebServer;