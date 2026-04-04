const Task = require('../models/Task');
const Room = require('../models/Room');

// @desc    Assign a task to an employee
// @route   POST /api/tasks
const assignTask = async (req, res) => {
  const { assignedTo, roomId, taskType, urgency } = req.body;

  try {
    const task = await Task.create({
      assignedTo,
      room: roomId,
      taskType,
      urgency
    });

    // When a task is assigned, we mark the room as "Needs Cleaning" or "Maintenance"
    const statusUpdate = taskType === 'Cleaning' ? 'Needs Cleaning' : 'Maintenance';
    await Room.findByIdAndUpdate(roomId, { currentStatus: statusUpdate });

    res.status(201).json({ message: "Task assigned successfully", task });
  } catch (error) {
    res.status(500).json({ message: "Task assignment failed", error: error.message });
  }
};

// @desc    Get tasks for the logged-in employee
// @route   GET /api/tasks/my-tasks
const getMyTasks = async (req, res) => {
  try {
    // We only show 'Pending' tasks so the list stays clean
    const tasks = await Task.find({ 
      assignedTo: req.user._id, 
      status: 'Pending' 
    }).populate('room', 'roomNumber type');
    
    res.json(tasks);
  } catch (error) {
    res.status(500).json({ message: "Could not fetch tasks", error: error.message });
  }
};

// @desc    Mark a task as completed and clear the room
const completeTask = async (req, res) => {
  // ✅ FIX: Changed 'taskId' to 'id' to match your route definition (/:id/complete)
  const { id } = req.params; 
  
  try {
    const task = await Task.findById(id);
    if (!task) return res.status(404).json({ message: "Task not found" });

    // 1. Update Task Status
    task.status = 'Completed';
    task.completedAt = Date.now();
    await task.save();

    // 2. Automatically set room back to Available!
    if (task.room) {
      await Room.findByIdAndUpdate(task.room, { currentStatus: 'Available' });
    }

    res.json({ message: "Task completed! Room is now clean and available." });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

// Add to module.exports:
module.exports = { assignTask, getMyTasks, completeTask };