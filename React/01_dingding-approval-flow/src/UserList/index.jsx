import React, { forwardRef, useImperativeHandle, useState } from 'react';

const UserList = forwardRef((props, ref) => {
  useImperativeHandle(ref, () => ({
    
  }));
  return (
    <div>
      <div>UserList</div>
    </div>
  )
})

export default UserList