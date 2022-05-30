const express = require('express');
const router = express.Router();
const authenticated = require('../models/authenticated');
const authorization = require('../models/authorization');
const meet = require('../models/meet');

const { body, validationResult } = require('express-validator');
const userSession = require("../models/userSession");
router.post('/',
    body('date').exists(),
    body('idUser').exists(),
    body('idSession').exists(),
    body('idUserSession').exists(),
    body('note').exists(),
    authenticated,
    async (req, res) => {
      const errors = validationResult(req);
      if (!errors.isEmpty()) {
        return res.status(400).json({ errors: errors.array() });
      }

      let result = await meet.add(req.body)
      res.status(result.status).json(result.msg)
});

router.delete('/',
    body('idUser').exists(),
    body('idSession').exists(),
    body('idUserSession').exists(),
    authenticated,
    async (req, res) => {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            return res.status(400).json({ errors: errors.array() });
        }
        let result = await meet.delete(req.body.idUser, req.body.idSession, req.body.idUserSession)
        res.status(result.status).json(result.msg)
    });

router.get('/:idUserSession&:idSession',
    authenticated,
    async (req, res) => {
        console.log(req.params)
        let result = await meet.get(req.params.idUserSession, req.params.idSession)
        res.status(result.status).json(result.msg)
    });


exports.initializeRoutes = () => {
  return router;
}
