const mongoose = require('mongoose');

const userSchema = new mongoose.Schema({
  name: { type: String, required: true },
  email: { type: String, required: true, unique: true },
  phone: { type: String, required: true },
  isVIP: { type: Boolean, default: false },
  preferences: {
    pillowType: { type: String, default: 'Standard' },
    dietaryRestrictions: [{ type: String }],
    preferredTemp: { type: Number, default: 22 }
  },
  bookingHistory: [{ type: mongoose.Schema.Types.ObjectId, ref: 'Booking' }]
}, { timestamps: true });

module.exports = mongoose.model('User', userSchema);