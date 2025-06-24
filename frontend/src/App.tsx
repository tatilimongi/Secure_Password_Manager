import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './contexts/AuthContext';
import { ToastProvider } from './contexts/ToastContext';
import LandingPage from './pages/LandingPage';
import SignInPage from './pages/SignInPage';
import LoginPage from './pages/LoginPage';
import SetupPage from './pages/SetupPage';
import Dashboard from './pages/Dashboard';
import ProtectedRoute from './components/ProtectedRoute';
import Toast from './components/Toast';

function App() {
  return (
    <AuthProvider>
      <ToastProvider>
        <Router>
          <div className="App">
            <Routes>
              <Route path="/" element={<LandingPage />} />
              <Route path="/signin" element={<SignInPage />} />
              <Route path="/login" element={<LoginPage />} />
              <Route path="/setup" element={<SetupPage />} />
              <Route 
                path="/dashboard" 
                element={
                  <ProtectedRoute>
                    <Dashboard />
                  </ProtectedRoute>
                } 
              />
            </Routes>
            <Toast />
          </div>
        </Router>
      </ToastProvider>
    </AuthProvider>
  );
}

export default App;