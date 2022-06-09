const express = require('express');
const { initializeConfigMiddlewares, initializeErrorMiddlwares } = require('./middlewares');
const authRoutes = require('../controllers/auth-routes');
const taskRoutes = require('../controllers/task-routes');
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
    this.app.use('/task', taskRoutes.initializeRoutes())
  }
}

module.exports = WebServer;