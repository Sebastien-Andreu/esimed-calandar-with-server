const express = require('express');
const router = express.Router();
const authenticated = require('../models/authenticated');
const task = require('../models/task');

const { body, validationResult } = require('express-validator');

router.post('/',
    body('name').exists().withMessage('name of task is require'),
    body('date').exists().withMessage('date of task is require'),
    body('time').exists().withMessage('time of task is require'),
    body('progress').exists().withMessage('progress of task is require'),
    body('idUser').exists().withMessage('id of user is require'),
    authenticated,
    async (req, res) => {
      const errors = validationResult(req);
      if (!errors.isEmpty()) {
        return res.status(400).json({ status: 400, errors: errors.array() });
      }

      console.log(req.body)

      let result = await task.add(req.body)
      res.status(result.status).json(result)
});


router.post('/update',
    body('id').exists().withMessage('id of task is require'),
    body('name').exists().withMessage('name of task is require'),
    body('date').exists().withMessage('date of task is require'),
    body('time').exists().withMessage('time of task is require'),
    body('progress').exists().withMessage('progress of task is require'),
    body('idUser').exists().withMessage('id of user is require'),
    authenticated,
    async (req, res) => {
      const errors = validationResult(req);
      if (!errors.isEmpty()) {
        return res.status(400).json({ status: 400, errors: errors.array() });
      }

      console.log(req.body)

      let result = await task.update(req.body)
      res.status(result.status).json(result)
});


router.delete('/',
    authenticated,
    async (req, res) => {
        let result = await task.delete(req.headers)
        res.status(result.status).json(result)
    });

router.get('/',
    authenticated,
    async (req, res) => {
        console.log(req.headers)
        let result = await task.get(req.headers["iduser"])
        console.log(result)
        res.status(200).json(result)
    });


exports.initializeRoutes = () => {
  return router;
}
