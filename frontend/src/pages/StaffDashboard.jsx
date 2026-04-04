import React, { useEffect, useState } from 'react';
import API from '../api/axios';
import { CheckCircle, Clock, LogOut, Loader2, Sparkles } from 'lucide-react';

const StaffDashboard = () => {
  const [tasks, setTasks] = useState([]);
  const [loading, setLoading] = useState(true);
  const user = JSON.parse(localStorage.getItem('user'));

  useEffect(() => {
    fetchMyTasks();
  }, []);

  const fetchMyTasks = async () => {
    try {
      const { data } = await API.get('/tasks/my-tasks');
      setTasks(data);
      setLoading(false);
    } catch (err) {
      console.error("Failed to load tasks");
      setLoading(false);
    }
  };

  const handleCompleteTask = async (taskId) => {
    try {
      await API.put(`/tasks/${taskId}/complete`);
      alert("Task marked as complete. Room status updated!");
      fetchMyTasks(); // Refresh the list
    } catch (err) {
      alert("Could not update task status.");
    }
  };

  return (
    <div className="min-h-screen bg-[#050510] text-white p-6 md:p-10 font-sans selection:bg-blue-500">
      {/* Header */}
      <div className="flex justify-between items-center border-b border-white/10 pb-6 mb-10">
        <div>
          <h1 className="text-2xl font-bold tracking-tight uppercase flex items-center gap-2">
            <Sparkles className="text-blue-400" size={24} /> Staff Hub
          </h1>
          <p className="text-gray-500 text-xs mt-1">Personnel: <span className="text-blue-300 font-mono">{user?.name}</span></p>
        </div>
        <button onClick={() => {localStorage.clear(); window.location.href='/';}} className="text-gray-400 hover:text-red-400 transition-colors flex items-center gap-2 cursor-pointer">
          <LogOut size={18} /> <span className="text-sm uppercase tracking-widest font-bold">Sign Out</span>
        </button>
      </div>

      <div className="max-w-3xl mx-auto">
        <h2 className="text-white text-lg mb-6 flex items-center gap-2 font-serif italic">
          <Clock size={20} className="text-blue-400" /> Active Assignments
        </h2>

        {loading ? (
          <div className="flex justify-center p-20"><Loader2 className="animate-spin text-blue-500" size={40} /></div>
        ) : tasks.length === 0 ? (
          <div className="bg-white/5 border border-dashed border-white/10 p-16 text-center rounded-3xl">
            <p className="text-gray-500 italic tracking-wide">"Excellence is not an act, but a habit."</p>
            <p className="text-xs text-gray-700 mt-2 uppercase">No pending tasks found.</p>
          </div>
        ) : (
          <div className="grid gap-4">
            {tasks.map(task => (
              <div key={task._id} className="bg-white/5 border border-white/5 p-6 rounded-2xl flex justify-between items-center hover:bg-white/[0.08] transition-all group border-l-4 border-l-blue-500">
                <div>
                  <div className="flex items-center gap-3 mb-1">
                    <span className="text-2xl font-bold font-mono">ROOM {task.room?.roomNumber}</span>
                  </div>
                  <p className="text-blue-400 text-xs font-bold uppercase tracking-[0.2em]">{task.taskType}</p>
                </div>

                <button 
                  onClick={() => handleCompleteTask(task._id)}
                  className="bg-blue-600 hover:bg-blue-500 text-white px-6 py-3 rounded-xl font-bold transition-all shadow-lg shadow-blue-900/40 cursor-pointer flex items-center gap-2 text-sm uppercase"
                >
                  <CheckCircle size={18} /> Finish Task
                </button>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default StaffDashboard;