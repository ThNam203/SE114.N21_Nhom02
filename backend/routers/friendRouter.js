const express = require('express')

const router = express.Router()
const friendController = require('../controllers/friendController')

router.route('/create-request').post(friendController.createNewFriendRequest)

module.exports = router
