const mongoose = require('mongoose');

const bookingSchema = new mongoose.Schema({
  guestName: { type: String, required: true },
  room: { type: mongoose.Schema.Types.ObjectId, ref: 'Room', required: true },
  checkIn: { type: Date, default: Date.now },
  checkOut: { type: Date },
  status: { type: String, enum: ['Active', 'Completed'], default: 'Active' }
}, { timestamps: true });

module.exports = mongoose.model('Booking', bookingSchema);