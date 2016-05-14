function [ E ] = ComputeError( pnts_gt, pnts_computed )
%COMPUTEERROR Summary of this function goes here
%   Detailed explanation goes here
    n_pnts = size(pnts_gt, 1);
    
    E = 0;
    for i = 1 : n_pnts
        % Adding square distance between points to the error.
        E = E + norm(pnts_gt(1:2, i) - pnts_computed(1:2, i));
    end
end

