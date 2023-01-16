
# COSC2657_A3

![Project Logo](https://github.com/TTrung224/BlueGit/blob/main/readme_logo.png)

## Team Members:

  1. Tran Quoc Trung (s3891724)
  2. Huynh Ky Thanh (s3884734)
  3. Ho Tran Minh Khoi (s3877653)
  4. Tran Minh Khoi (s3916827)
  

## Work distribution:

  1. Tran Quoc Trung (s3891724): Adapters, Firebase store manage, design UX/UI, code Chat, Message, Admin and Service  -25%
  2. Huynh Ky Thanh (s3884734): Adapters, Firebase store manage, design UX/UI, code Log in, Register, User, Cart, Product, Order and Broadcast  -25%
  3. Ho Tran Minh Khoi (s3877653): Adapters, Firebase store manage, design UX/UI, code Product, Cart and User -25%
  4. Tran Minh Khoi (s3916827): Adapters, Firebase store manage, design UX/UI, code Voucher, Admin   -25%
 

## Functionalities:

  1. **Authentication**: Login by Gmail, email and password. *NOTE: If user uses Google login after registering with that google email, the Google Auth will overwrite the previous authentication data. User data will remain but user will have to use Google Auth from that point on to authenticate their account.*
  2. **Admin manage vouchers**: Admin can add, view, and disable voucher.
  3. **Admin manage products**: Admin can view product list, disable product.
  4. **Admin manage account**: Admin can view the list of users in system.
  5. **Admin manage orders**: Admin can view user orders.
  6. **User add products**: User can add products if provided appropriate information.
  7. **User view products**: User can view list of available products posted by other users.
  8. **User search products**: User can search for a product by typing relating keywords.
  9. **User buy products**: User can select a product, choose the quantity then add to cart. *NOTE: if items in cart belong to different sellers, different orders will be created for each of the sellers.*
  10. **Voucher**: Voucher decreases total price of cart. Decrease amounts will vary for each order, the deducted amount will be deducted from the admin's profit.
  11. **User chat**: User can chat with the shop owner or with customer.
  12. **User cart**: The products that the user buy will be store in their cart, in the cart the user need to provide shipping info, add voucher (if wanted) then order the products.
  13. **User business**: User can list products to sell and view the sold products. *NOTE: 20% of the sold amount will be added to the admin's account, ensure profit to system runner.*
  14. **User order**: User can view the list of the product they have ordered.
  15.  **Order status**: Order can be set to "delivering" by seller, and set to "completed" by customer.
  16. **User account**: User update account information(user name, phone number, add new shipping info).
  17. **Log out**: Admin and user can log out of their account.
  18. **Internet Connection Broadcast Receiver**: Check for internet connection while using app, which is required for database access.
  19. **Notification Service**: Listen for new incoming message, new order and update of order status, then notify the appropriate user accordingly.
  

## Technology used:

    Firebase Firestore - Database and Data Persistence
    Firebase Authentication - User Authentication + Google Sign In API
    Firebase Storage - Storing user's personal files (pictures)
    Picasso Library - Loading images into ImageViews with URLs
    Anroid Studio - IDE
    Github - Version control
    

## Open issues:

* At the moment, the system is not very scalable. If we encounter too much traffic, where there are too many users and user's activities, the system will definitely suffer from long load time due to time to get/put data from and to FireStore. 
* For the chat function, due to the adapter implementing Firestore ViewHolder, we are unable to scroll the view to the end of the chat room. Because of this, if the user has a very long conversation, it might be inconvenient to have to scroll down every time the activity is started.
* The notification for chat will be shown to the user eventhough they are already in the chat activity.

## Known Bugs:

  For the Login using Google, on some developing computers, it might occasionally return an error while using this function. At the moment, we are not sure what the exact cause of this, but we suspect that it is due to the emulator not working properly. The reason for this is because the function would work again after resetting or wiping the emulator's data, as well as the function working fine in other developing computers.
