const mongoose = require('mongoose');

const roomSchema = new mongoose.Schema({
  roomNumber: { type: String, required: true, unique: true },
  type: { type: String, enum: ['Standard', 'Deluxe', 'Suite', 'Penthouse'], required: true },
  basePrice: { type: Number, required: true },
  currentStatus: { 
    type: String, 
    enum: ['Available', 'Occupied', 'Needs Cleaning', 'Maintenance'], 
    default: 'Available' 
  },
  inventory: {
    towels: { type: Number, default: 4 },
    miniBarStocked: { type: Boolean, default: true }
  }
}, { timestamps: true });

module.exports = mongoose.model('Room', roomSchema);