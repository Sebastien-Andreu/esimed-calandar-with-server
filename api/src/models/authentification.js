const jwt = require('jsonwebtoken');
const daoUser = require("../dao/DAOUsers"); //require
const md5 = require('md5');
const secret = process.env.JWT_SECRET || 'secretOfAppli__9525+';

module.exports.login = async (body, res) => {
    const user = await daoUser.findByPseudo(body.pseudo);
    if (user.length === 0) return { status: 401, message: 'This pseudo does not correspond to any user'}

    if (md5(body.password) === user[0].password) {
        return { status: 200, message: jwt.sign({ id: user[0].id, email: user[0].email, pseudo: user[0].pseudo}, secret, { expiresIn: '50min' })}
    } else {
        return { status: 401, message: 'The password is incorrect'}
    }
};

module.exports.signup = async (body) => {
    const verifyMail = await daoUser.findByEmail(body.email)
    if (verifyMail.length !== 0) return { status: 403, message: 'Email address already exist'};

    console.log(verifyMail)

    const verifyPseudo = await daoUser.findByPseudo(body.pseudo)
    if (verifyPseudo.length !== 0) return { status: 403, message: 'Pseudo already exist'};

    console.log(verifyPseudo)

    await daoUser.add(body.email, body.pseudo, md5(body.password))
    const user = await daoUser.findByEmail(body.email);
    if (!user) return { status: 403, message: 'User can\'t be create'};
    return { status: 200, message: 'User created !'};
};