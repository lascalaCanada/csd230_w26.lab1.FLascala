// Turns an Axios error into a short message for the screen.
// 403 = the account is signed in but is not allowed to do this (no logout happens).
// Only the message is read: the whole error object could contain authentication data.
export default function errorText(err) {
  if (err.response?.status === 403) return 'You do not have permission to do this.';
  return err.message;
}
