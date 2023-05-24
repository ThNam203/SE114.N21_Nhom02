const FriendRequest = require('../models/FriendRequest')
const Notification = require('../models/Notification')
const User = require('../models/User')
const AppError = require('../utils/AppError')
const asyncCatch = require('../utils/asyncCatch')

// const sendNotificationOnReply = (sender, receiver, isAccept) => {
//     let message
//     if (isAccept) message = `${receiver.name} accepted your friend request`
//     else message = `${receiver.name} denied your friend request`

//     Notification.create({
//         userId: sender._id,
//         notificationType: 'FriendRequest',
//         content: message,
//     })
// }

const sendNotificationOnRequest = async (senderId, receiverId) => {
    const sender = await User.findOne({ _id: senderId })
    const content = `${sender.name} has sent you a friend request`

    Notification.create({
        userId: receiverId,
        notificationType: 'FriendRequest',
        content,
    })
}

exports.createNewFriendRequest = asyncCatch(async (req, res, next) => {
    const { senderEmail, receiverEmail } = req.body

    if (receiverEmail === senderEmail)
        return next(new AppError('Unable to add friend to yourself', 400))

    const sender = await User.findOne({ email: senderEmail })
    const receiver = await User.findOne({ email: receiverEmail })
    if (!receiver || !sender) return next(new AppError(`Email not found`, 400))

    // check if friend request is pending
    const isExisted = await FriendRequest.findOne({
        senderId: sender._id,
        receiverId: receiver._id,
    })

    if (isExisted)
        return next(new AppError('The request is already on pending', 400))

    // check if already been friend
    const isFriended = await User.findOne({
        connections: { $in: [receiver._id] },
    })
    if (isFriended) return next(new AppError('Already friend', 400))

    // create the request in db
    const newFriendRequest = await FriendRequest.create({
        senderId: sender._id,
        receiverId: receiver._id,
    })

    if (!newFriendRequest)
        return next(new AppError('Unable to create new friend request', 500))

    sendNotificationOnRequest(sender._id, receiver._id)

    res.status(200).json(newFriendRequest)
})
