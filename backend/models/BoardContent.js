/* eslint-disable prettier/prettier */
const mongoose = require('mongoose')

const cellBaseSchema = new mongoose.Schema({
    content: {
        type: String,
    },
})

const cellTextSchema = new mongoose.Schema()

const cellStatusSchema = new mongoose.Schema({
    contents: {
        type: [{ type: String }],
    },
    colors: {
        type: [{ type: String }],
    },
})

const cellNumberSchema = new mongoose.Schema()

const cellDateSchema = new mongoose.Schema({
    year: Number,
    month: Number,
    day: Number,
    hour: Number,
    minute: Number,
})

const cellTimelineSchema = new mongoose.Schema({
    startYear: Number,
    startMonth: Number,
    startDay: Number,
    endYear: Number,
    endMonth: Number,
    endDay: Number,
})

const cellUpdateSchema = new mongoose.Schema()

const cellUserSchema = new mongoose.Schema()

const cellBaseModel = mongoose.model('Cell', cellBaseSchema)

// Create sub-schemas for each cell type
const CellText = cellBaseModel.discriminator('Text', cellTextSchema)
const CellStatus = cellBaseModel.discriminator('Status', cellStatusSchema)
const CellNumber = cellBaseModel.discriminator('Number', cellNumberSchema)
const CellDate = cellBaseModel.discriminator('Date', cellDateSchema)
const CellTimeline = cellBaseModel.discriminator('Timeline', cellTimelineSchema)
const CellUpdate = cellBaseModel.discriminator('Update', cellUpdateSchema)
const CellUser = cellBaseModel.discriminator('User', cellUserSchema)

module.exports = {
    CellText,
    CellStatus,
    CellNumber,
    CellDate,
    CellTimeline,
    CellUpdate,
    CellUser
}
