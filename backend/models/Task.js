const mongoose = require('mongoose');

const taskSchema = new mongoose.Schema({
  assignedTo: { type: mongoose.Schema.Types.ObjectId, ref: 'Employee', required: true },
  room: { type: mongoose.Schema.Types.ObjectId, ref: 'Room', required: true },
  taskType: { type: String, enum: ['Cleaning', 'Room Service', 'Maintenance'], required: true },
  urgency: { type: String, enum: ['Normal', 'High', 'Critical'], default: 'Normal' },
  checklist: [{
    item: { type: String },
    isCompleted: { type: Boolean, default: false }
  }],
  status: { type: String, enum: ['Pending', 'In Progress', 'Completed', 'Escalated'], default: 'Pending' },
  completedAt: { type: Date }
}, { timestamps: true });

module.exports = mongoose.model('Task', taskSchema);