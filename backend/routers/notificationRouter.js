const express = require('express')

const router = express.Router()
const notificationController = require('../controllers/notificationController')

router
    .route('/notification/:receiverId')
    .get(notificationController.getNotificationByReceiverId)

router
    .route('/delete-notification/:notificationId')
    .get(notificationController.deleteNotificationById)

module.exports = router
