const express = require('express');
const router = express.Router();
const Auth = require('../models/authentification');
const { body, validationResult } = require('express-validator');

router.post('/login',
    body('pseudo').exists().withMessage('pseudo is require'),
    body('password').exists().withMessage('password is require'),
    async (req, res) => {
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
        return res.status(400).json({ status: 400, message: errors.array() });
    }

    const result = await Auth.login(req.body)
    console.log(result)
    res.status(result.status).json(result)
});

router.post('/signup',
    body('email').exists().isEmail().withMessage('Email is require'),
    body('pseudo').exists().withMessage('Pseudo is require'),
    body('password').exists().withMessage('password is require'),
    async (req, res) => {
        const errors = validationResult(req);
        if (!errors.isEmpty()) {
            return res.status(400).json({ status: 400, message: errors.array() });
        }

        const result = await Auth.signup(req.body)
        console.log(result)
        res.status(result.status).json(result)

    });

exports.initializeRoutes = () => {
    return router;
}