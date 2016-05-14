function [ pnts_gt, pnts_computed ] = ComputeTestPoints( H_gt, H_computed )
%COMPUTETESTPOINTS Summary of this function goes here
%   Detailed explanation goes here
    n_pnts = 10; % Is this enough?
    pnts_rnd = [randn(2, n_pnts); ones(1, n_pnts)];
    
    % normalizing
    for i = 1 : 10
        pnts_rnd(1:2, i) = pnts_rnd(1:2, i) / norm(pnts_rnd(1:2, i));
    end
    
    pnts_gt = H_gt * pnts_rnd;
    pnts_computed = H_computed * pnts_rnd;

end

