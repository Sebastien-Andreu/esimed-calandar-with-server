const db = require('../models/db')

const add = async function (name, date, time, progress, idUser) {
    return db.insert({name:name, date:date, time:time, progress:progress, idUser:idUser}).into('Task')
}

const update = async function (id, name, date, time, progress, idUser) {
    return db.from('Task').where({id: id, idUser: idUser}).update({name:name, date:date, time:time, progress:progress})
}


const remove = async function (id, idUser) {
    return db.delete().from('Task').where({id: id, idUser: idUser})
}

const get = async function (idUser) {
    return db.select('*').from('Task').where({idUser: idUser})
}

module.exports = {
    add,
    remove,
    get,
    update
}