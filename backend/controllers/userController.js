const User = require('../models/User')
const asyncCatch = require('../utils/asyncCatch')
const AppError = require('../utils/AppError')
const s3Controller = require('./awsS3Controllers')

exports.updateProfileImage = asyncCatch(async (req, res, next) => {
    if (!req.file || !req.file.location)
        return next(new AppError('Unable to upload profile image', 500))

    const { userId } = req.params
    const user = await User.findById(userId)
    if (user.profileImagePath)
        s3Controller.deleteOldProfileImage(user.profileImagePath)

    user.profileImagePath = req.file.location
    await user.save()

    res.status(200).json({ imagePath: req.file.location })
})

exports.getUserById = asyncCatch(async (req, res, next) => {
    const { userId } = req.params
    const user = await User.findById(userId)
    if (!user) return next(new AppError('No user found!', 400))

    res.status(200).json(user)
})

exports.getAllUsers = asyncCatch(async (req, res, next) => {
    const user = await User.find({})
    if (!user) return next(new AppError('No email found!', 400))

    res.status(200).json(user)
})

exports.getUserByEmail = asyncCatch(async (req, res, next) => {
    const { email } = req.params
    const user = await User.findOne({ email: email })
    if (!user) return next(new AppError('No email found!', 400))

    res.status(200).json(user)
})

exports.updateUser = asyncCatch(async (req, res, next) => {
    const { _id } = req.body
    const updatedUser = await User.findByIdAndUpdate(_id, req.body, {
        new: true,
        runValidators: true,
    })
    if (!updatedUser) return next(new AppError('No user found!', 400))

    res.status(200).json(updatedUser)
})

exports.addNewUser = asyncCatch(async (req, res, next) => {
    const newUser = new User({
        name: req.body.name,
        email: req.body.email,
        password: req.body.password,
        phoneNumber: req.body.phoneNumber,
        profileImagePath: req.body.profileImagePath,
        location: req.body.location,
    })

    const savedUser = await newUser.save()
    res.status(200).json({
        message: 'user was saved!!',
    })

    if (!savedUser) return next(new AppError('Save new user ERROR!', 400))

    res.status(200).json(savedUser)
})
