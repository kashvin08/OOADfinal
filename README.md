# Object Oriented Analysis & Design 

-------------------------------------------------------------------------------
A parking lot manager system, the core ParkingLotManager is based on the singleton design pattern, specifically the Bill Pugh variant. There are 5 floors and each floor has several parking spaces allocated for certain membership types such as Handicapped, Reserved, Compact and, Regular. In regards to the fines and payments, strategy pattern is used to prevent the modification of the core logic.

Key Features: 
-------------------------------------------------------------------------------
Multi-Level Management: Manages 50 spots across 5 floors with specific classifications (Compact, Regular, Reserved, Handicapped).

Smart Spot Allocation: Features a priority logic for handicapped users, granting them access to ALL spot types.

Financial Auditing: Tracks total revenue and maintains a ledger of unpaid fines, allowing the system to charge violators
