const mongoose = require('mongoose');
const bcrypt = require('bcrypt');

const employeeSchema = new mongoose.Schema({
  name: { type: String, required: true },
  username: { type: String, required: true, unique: true },
  password: { type: String, required: true },
  role: { 
    type: String, 
    enum: ['admin', 'reception', 'cleaner', 'chef', 'manager', 'guest'], 
    required: true 
  },
  department: { type: String },
  performanceRating: { type: Number, default: 5.0, max: 5.0 },
  isActive: { type: Boolean, default: true }
}, { timestamps: true });

// ✅ CORRECTED: Removed 'next' because we are using 'async'
employeeSchema.pre('save', async function () {
  if (!this.isModified('password')) return; // No next() needed here

  const salt = await bcrypt.genSalt(10);
  this.password = await bcrypt.hash(this.password, salt);
  
  // No next() call at the end either!
});

module.exports = mongoose.model('Employee', employeeSchema);