const daoTask = require("../dao/DAOTask");

module.exports.add = async (body) => {
    await daoTask.add(body.name, body.date, body.time, body.progress, body.idUser)
    return { status: 200, message: 'task created'}
};

module.exports.update = async (body) => {
    await daoTask.update(body.id, body.name, body.date, body.time, body.progress, body.idUser)
    return { status: 200, message: 'task updated'}
};

module.exports.delete = async (headers) => {
    await daoTask.remove(headers['id'], headers['iduser'])
    return { status: 200, message: 'task deleted'}
};

module.exports.get = async (idUser) => {
    return await daoTask.get(idUser)
};