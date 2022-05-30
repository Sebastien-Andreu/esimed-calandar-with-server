const db = require('../models/db')

/**
 * return one User with mail from the database
 * @params {string} mail - mail of User
 */
const findByMail = async function (mail) {
    return db.select('*').from('Users').where({mail: mail}).limit(1)
}

/**
 * return one User with pseudo from the database
 * @params {string} pseudo - pseudo of User
 */
const findByPseudo= async function (pseudo) {
    return db.select('*').from('Users').where({pseudo: pseudo}).limit(1)
}

// /**
//  * return one User with id from the database
//  * @params {int} id - id of User
//  */
// const findWithId = async function (id) {
//     return db.select('*').from('Users').where({id: id})
// }

/**
 * add new User in database
 * @params {string} mail - mail of user
 * @params {string} pseudo - pseudo of user
 * @params {string} password - password of user
 */
const add = async function (mail, pseudo, password) {
    await db.insert({mail: mail, pseudo: pseudo, password: password}).into('Users')
}

/**
 * delete User in the database
 * @params {int} id - id of User
 */
const remove = async function (id) {
    await db.delete().from('Users').where({id: id})
}

/**
 * update User in database
 * @params {int} id - id of User
 * @params {string} mail - mail of user
 * @params {string} pseudo - pseudo of user
 * @params {string} password - password of user
 */
const update = async function (id, mail, pseudo, password) {
    await db.from("Users").where({id: id}).update({mail: mail, pseudo: pseudo, password: password})
}

module.exports = {
    findByEmail: findByMail,
    findByPseudo,
    add,
    remove,
    update
}