// UI hint only: decoding is NOT signature verification or server authorization.
// Spring checks the signature, the expiration and the permissions on every request.
export function hasRole(token, role) {
  try {
    // A JWT has three parts separated by dots: header.payload.signature
    if (typeof token !== 'string' || token.split('.').length !== 3) return false;
    const part = token.split('.')[1];
    // The payload is Base64URL: switch to normal Base64 and restore the "=" padding
    const base64 = part.replace(/-/g, '+').replace(/_/g, '/');
    const claims = JSON.parse(atob(base64.padEnd(Math.ceil(base64.length / 4) * 4, '=')));
    // Test membership (the array can also hold other entries), and never trust a non-array
    return Array.isArray(claims?.roles) && claims.roles.includes(role);
  } catch { return false; }
}
